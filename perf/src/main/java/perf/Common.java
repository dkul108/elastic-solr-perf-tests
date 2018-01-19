package perf;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Random;

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

    static int randomCount() {
        return random(100);
    }
}
