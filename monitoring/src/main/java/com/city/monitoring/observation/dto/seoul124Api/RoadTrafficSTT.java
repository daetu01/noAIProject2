package com.city.monitoring.observation.dto.seoul124Api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RoadTrafficSTT {
    @JsonProperty("LINK_ID")
    private String linkId;

    @JsonProperty("ROAD_NM")
    private String roadNm;

    @JsonProperty("START_ND_CD")
    private String startNdCd;

    @JsonProperty("START_ND_NM")
    private String startNdNm;

    @JsonProperty("START_ND_XY")
    private String startNdXY;

    @JsonProperty("END_ND_CD")
    private String endNdCd;

    @JsonProperty("END_ND_NM")
    private String endNdNm;

    @JsonProperty("END_ND_XY")
    private String endNdXY;

    @JsonProperty("DIST")
    private float dist;

    @JsonProperty("SPD")
    private float speed;

    @JsonProperty("IDX")
    private String idx;

    @JsonProperty("XYLIST")
    private String xyList;
}
