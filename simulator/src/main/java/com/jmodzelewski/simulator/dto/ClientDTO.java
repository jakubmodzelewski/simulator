package com.jmodzelewski.simulator.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {
    protected Long id;

    int x;
    int y;

}
