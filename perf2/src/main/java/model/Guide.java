
package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "channelId",
        "title",
        "start",
        "season",
        "releaseYear",
        "productionStudio",
        "content",
        "cast",
        "end"
})
public class Guide {

    @Field
    @JsonProperty("channelId")
    private String channelId;
    @Field
    @JsonProperty("title")
    private String title;
    @Field
    @JsonProperty("start")
    private long start;
    @Field
    @JsonProperty("season")
    private int season;
    @Field
    @JsonProperty("releaseYear")
    private int releaseYear;
    @Field
    @JsonProperty("productionStudio")
    private String productionStudio;
    @Field
    @JsonProperty("content")
    @Valid
    private Content content;
    @Field
    @JsonProperty("cast")
    @Valid
    private List<Cast> cast = new ArrayList<Cast>();
    @Field
    @JsonProperty("end")
    private long end;

    /**
     * @return The channelId
     */
    @JsonProperty("channelId")
    public String getChannelId() {
        return channelId;
    }

    /**
     * @param channelId The channelId
     */
    @JsonProperty("channelId")
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Guide withChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    /**
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Guide withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @return The start
     */
    @JsonProperty("start")
    public long getStart() {
        return start;
    }

    /**
     * @param start The start
     */
    @JsonProperty("start")
    public void setStart(long start) {
        this.start = start;
    }

    public Guide withStart(long start) {
        this.start = start;
        return this;
    }

    /**
     * @return The season
     */
    @JsonProperty("season")
    public int getSeason() {
        return season;
    }

    /**
     * @param season The season
     */
    @JsonProperty("season")
    public void setSeason(int season) {
        this.season = season;
    }

    public Guide withSeason(int season) {
        this.season = season;
        return this;
    }

    /**
     * @return The releaseYear
     */
    @JsonProperty("releaseYear")
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * @param releaseYear The releaseYear
     */
    @JsonProperty("releaseYear")
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Guide withReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    /**
     * @return The productionStudio
     */
    @JsonProperty("productionStudio")
    public String getProductionStudio() {
        return productionStudio;
    }

    /**
     * @param productionStudio The productionStudio
     */
    @JsonProperty("productionStudio")
    public void setProductionStudio(String productionStudio) {
        this.productionStudio = productionStudio;
    }

    public Guide withProductionStudio(String productionStudio) {
        this.productionStudio = productionStudio;
        return this;
    }

    /**
     * @return The content
     */
    @JsonProperty("content")
    public Content getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    @JsonProperty("content")
    public void setContent(Content content) {
        this.content = content;
    }

    public Guide withContent(Content content) {
        this.content = content;
        return this;
    }

    /**
     * @return The cast
     */
    @JsonProperty("cast")
    public List<Cast> getCast() {
        return cast;
    }

    /**
     * @param cast The cast
     */
    @JsonProperty("cast")
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public Guide withCast(List<Cast> cast) {
        this.cast = cast;
        return this;
    }

    /**
     * @return The end
     */
    @JsonProperty("end")
    public long getEnd() {
        return end;
    }

    /**
     * @param end The end
     */
    @JsonProperty("end")
    public void setEnd(long end) {
        this.end = end;
    }

    public Guide withEnd(long end) {
        this.end = end;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
