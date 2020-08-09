package com.jmodzelewski.simulator.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class NodeDTO {
    protected Long id;

    int x;
    int y;
}
