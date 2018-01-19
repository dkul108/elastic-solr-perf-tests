package perf;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.collection.CharSeq;
import io.vavr.collection.Iterator;
import model.*;
import model.Package;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public interface Common {

    String guide = "guide";

    String keyword = ".keyword";

    static String kw(final String name) {
        return name + keyword;
    }

    String channelId = "channelId";
    String channelId_kw = kw(channelId);

    String start = "start";
    String end = "end";

    String contentCategory = "content.category";
    String contentCategory_kw = kw(contentCategory);

    String contentGenre = "content.genre";
    String contentGenre_kw = kw(contentGenre);

    String contentPackage = "content.packages.name";
    String contentPackage_kw = kw(contentPackage);

    String contentCountry = "content.packages.countries";
    String contentCountry_kw = kw(contentCountry);

    String contentConcurrency = "content.packages.contract.concurrency";

    String category = "Series";
    String genre = "Action";
    String packageName = "Europe";
    String country = "ru";

    ThreadLocal<Random> random = ThreadLocal.withInitial(() -> new Random());

    static int random(final int range) {
        return random.get().nextInt(range);
    }

    static String randomChannel() {
        return "CHAN_" + random(490);
    }

    static String[] randomChannels() {
        return new String[]{randomChannel(), randomChannel()};
    }

    static long timeMs(final LocalDateTime time) {
        return time.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    LocalDateTime NOW = LocalDateTime.of(
            2017, Month.NOVEMBER, 1,
            12, 15, 0);

    static long now() {
        return timeMs(NOW);
    }

    static long pastSeconds(final int seconds) {
        return timeMs(NOW.minusSeconds(seconds));
    }

    static long futureSeconds(final int seconds) {
        return timeMs(NOW.plusSeconds(seconds));
    }

    static long pastMinutes(final int minutes) {
        return timeMs(NOW.minusMinutes(minutes));
    }

    static long futureMinutes(final int minutes) {
        return timeMs(NOW.plusMinutes(minutes));
    }

    static long pastHours(final int hours) {
        return timeMs(NOW.minusHours(hours));
    }

    static long futureHours(final int hours) {
        return timeMs(NOW.plusHours(hours));
    }

    static long pastDays(final int days) {
        return timeMs(NOW.minusDays(days));
    }

    static long futureDays(final int days) {
        return timeMs(NOW.plusDays(days));
    }

    static long randomNow() {
        return futureHours(random(120));
    }

    static long randomStart() {
        return futureHours(random(120) + 120);
    }

    static long randomEnd() {
        return futureHours(random(120) + 240);
    }

    static int randomCount() {
        return random(100);
    }

    int chars = 'z' - 'a';

    static char randomChar() {
        return (char) ('a' + random(chars));
    }

    static String randomStr(final int len) {
        return CharSeq.fill(len, Common::randomChar).toString();
    }

    static String randomStr() {
        return randomStr(10);
    }

    static <T> List<T> randomList(final int len, Supplier<T> s) {
        return Iterator.fill(len, s).toJavaList();
    }

    static List<String> randomStrs(final int len) {
        return randomList(len, Common::randomStr);
    }

    static Guide randomGuide() {
        return new Guide()
                .withChannelId(randomStr())
                .withStart(randomStart())
                .withEnd(randomEnd())
                .withTitle(randomStr())
                .withReleaseYear(2017)
                .withSeason(random(10))
                .withProductionStudio(randomStr())
                .withCast(randomList(3, () -> new Cast()
                        .withFirstName(randomStr())
                        .withLastName(randomStr())
                        .withRole(randomStr())
                        .withResourceType(randomStr())
                ))
                .withContent(new Content()
                        .withCategory(randomStrs(2))
                        .withGenre(randomStrs(2))
                        .withLanguages(randomStrs(2))
                        .withPackages(randomList(2, () -> new Package()
                                .withName(randomStr())
                                .withCountries(randomStrs(2))
                                .withContract(new Contract()
                                        .withConcurrency(random(4)))
                        ))
                );
    }

    ObjectMapper jackson = new ObjectMapper();

    static <T> String toJson(final T t) throws Exception {
        return jackson.writeValueAsString(t);
    }

    static String randomGuideJson() throws Exception {
        return toJson(randomGuide());
    }

    static Generator generator(final Scenario<?, ?> scenario) {
        return new Generator(scenario);
    }

    class Generator {
        private final Scenario<?, ?> scenario;
        private final AtomicBoolean start = new AtomicBoolean(false);
        private final AtomicBoolean stop = new AtomicBoolean(false);
        private final ExecutorService executor = Executors.newSingleThreadExecutor();

        public Generator(final Scenario<?, ?> scenario) {
            this.scenario = scenario;
        }

        public void start() {
            if (!start.get()) {
                executor.submit(() -> {
                    while (!stop.get() && !Thread.interrupted())
                        scenario.write();
                    return true;
                });
                start.set(true);
            }
        }

        public void stop() throws InterruptedException {
            if (start.get() && !stop.get()) {
                stop.set(true);
                executor.shutdownNow();
                executor.awaitTermination(1, TimeUnit.SECONDS);
            }
        }

    }
}
