package com.test.t1.dto;

import com.test.t1.model.Status;
import jakarta.annotation.Nullable;

public record TaskRequest(String title, String description, int userId, @Nullable Status status) {

}
