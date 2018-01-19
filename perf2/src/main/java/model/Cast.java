
package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "role",
        "resourceType",
        "firstName",
        "lastName"
})
public class Cast {

    @Field
    @JsonProperty("role")
    private String role;
    @Field
    @JsonProperty("resourceType")
    private String resourceType;
    @Field
    @JsonProperty("firstName")
    private String firstName;
    @Field
    @JsonProperty("lastName")
    private String lastName;

    /**
     * @return The role
     */
    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    /**
     * @param role The role
     */
    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    public Cast withRole(String role) {
        this.role = role;
        return this;
    }

    /**
     * @return The resourceType
     */
    @JsonProperty("resourceType")
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType The resourceType
     */
    @JsonProperty("resourceType")
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Cast withResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    /**
     * @return The firstName
     */
    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The firstName
     */
    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Cast withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * @return The lastName
     */
    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The lastName
     */
    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Cast withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
