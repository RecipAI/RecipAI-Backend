package com.recipAI.server.domain.chat.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.recipAI.server.common.utils.Serializer.deserializeToMap;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenusResponse {
    private Map<String, List<String>> responseMenus;

    public MenusResponse(String jsonContent) {
        this.responseMenus = deserializeToMap(jsonContent);
    }

}
