package com.city.monitoring.observation.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class TrafficSTT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traffic_info_id")
    private TrafficInfo trafficInfo;

    private String linkId;

    private String roadNm;

    private String startNdCd;

    private String startNdNm;

    private float startNdX;

    private float startNdY;

    private String endNdCd;

    private String endNdNm;

    private float endNdX;

    private float endNdY;

    private float dist;

    private float speed;

    private String idx;

    @OneToMany(
            mappedBy = "trafficSTT",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TrafficSTTXY> xyList;

    public void addXY(TrafficSTTXY xy) {
        this.xyList.add(xy);
        xy.setTrafficSTT(this);
    }
}
