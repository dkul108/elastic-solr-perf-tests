
package model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "languages",
    "genre",
    "category",
    "packages"
})
public class Content {

    @Field
    @JsonProperty("languages")
    @Valid
    private List<String> languages = new ArrayList<String>();
    @Field
    @JsonProperty("genre")
    @Valid
    private List<String> genre = new ArrayList<String>();
    @Field
    @JsonProperty("category")
    @Valid
    private List<String> category = new ArrayList<String>();
    @Field
    @JsonProperty("packages")
    @Valid
    private List<Package> packages = new ArrayList<Package>();

    /**
     * 
     * @return
     *     The languages
     */
    @JsonProperty("languages")
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * 
     * @param languages
     *     The languages
     */
    @JsonProperty("languages")
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Content withLanguages(List<String> languages) {
        this.languages = languages;
        return this;
    }

    /**
     * 
     * @return
     *     The genre
     */
    @JsonProperty("genre")
    public List<String> getGenre() {
        return genre;
    }

    /**
     * 
     * @param genre
     *     The genre
     */
    @JsonProperty("genre")
    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public Content withGenre(List<String> genre) {
        this.genre = genre;
        return this;
    }

    /**
     * 
     * @return
     *     The category
     */
    @JsonProperty("category")
    public List<String> getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    @JsonProperty("category")
    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Content withCategory(List<String> category) {
        this.category = category;
        return this;
    }

    /**
     * 
     * @return
     *     The packages
     */
    @JsonProperty("packages")
    public List<Package> getPackages() {
        return packages;
    }

    /**
     * 
     * @param packages
     *     The packages
     */
    @JsonProperty("packages")
    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public Content withPackages(List<Package> packages) {
        this.packages = packages;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
