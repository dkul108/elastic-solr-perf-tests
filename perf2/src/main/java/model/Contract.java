
package model;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "concurrency"
})
public class Contract {

    @Field
    @JsonProperty("concurrency")
    private int concurrency;

    /**
     * 
     * @return
     *     The concurrency
     */
    @JsonProperty("concurrency")
    public int getConcurrency() {
        return concurrency;
    }

    /**
     * 
     * @param concurrency
     *     The concurrency
     */
    @JsonProperty("concurrency")
    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public Contract withConcurrency(int concurrency) {
        this.concurrency = concurrency;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
