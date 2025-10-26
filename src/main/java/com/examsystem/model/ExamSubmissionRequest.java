package com.examsystem.model;

import lombok.Data;

/**
 * DTO for exam submission.
 */
@Data
public class ExamSubmissionRequest {
    private Long examId;
    private String[] answers;
}
