
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
    "name",
    "countries",
    "contract"
})
public class Package {

    @Field
    @JsonProperty("name")
    private String name;
    @Field
    @JsonProperty("countries")
    @Valid
    private List<String> countries = new ArrayList<String>();
    @Field
    @JsonProperty("contract")
    @Valid
    private Contract contract;

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Package withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The countries
     */
    @JsonProperty("countries")
    public List<String> getCountries() {
        return countries;
    }

    /**
     * 
     * @param countries
     *     The countries
     */
    @JsonProperty("countries")
    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public Package withCountries(List<String> countries) {
        this.countries = countries;
        return this;
    }

    /**
     * 
     * @return
     *     The contract
     */
    @JsonProperty("contract")
    public Contract getContract() {
        return contract;
    }

    /**
     * 
     * @param contract
     *     The contract
     */
    @JsonProperty("contract")
    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Package withContract(Contract contract) {
        this.contract = contract;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
