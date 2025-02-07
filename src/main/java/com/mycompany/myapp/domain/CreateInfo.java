package com.mycompany.myapp.domain;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mycompany.myapp.json.Instant2MongoDateSerializer;
import com.mycompany.myapp.json.MongoDate2InstantDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@JsonInclude(Include.NON_NULL)
public class CreateInfo {

    @JsonProperty("user")
    @Field("user")
    private RefType user;

    @JsonProperty("created_date")
    @Field("created_date")
    @JsonSerialize(using = Instant2MongoDateSerializer.class)
    @JsonDeserialize(using = MongoDate2InstantDeserializer.class)
	private Instant createdDate;
}
