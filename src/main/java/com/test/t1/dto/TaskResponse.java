package com.test.t1.dto;

import com.test.t1.model.Status;

public record TaskResponse(Long id, String title, String description, int userId, Status status) {

}
