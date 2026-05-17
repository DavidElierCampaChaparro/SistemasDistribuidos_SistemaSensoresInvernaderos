/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package requests;

/**
 *
 * @author crazy
 */
public class ReportResponse {
    private final String jobId;
    private final String status;
    private final String message;
    private final byte[] pdfBytes;

    public ReportResponse(String jobId, String status, String message, byte[] pdfBytes) {
        this.jobId = jobId;
        this.status = status;
        this.message = message;
        this.pdfBytes = pdfBytes;
    }

    public static ReportResponse forStatus(String jobId, String status, String message) {
        return new ReportResponse(jobId, status, message, null);
    }

    public static ReportResponse forPdf(String jobId, byte[] pdfBytes) {
        return new ReportResponse(jobId, "ready", null, pdfBytes);
    }

    public String getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getPdfBytes() {
        return pdfBytes;
    }

    public boolean isReady() {
        return "ready".equalsIgnoreCase(status);
    }

    public boolean isError() {
        return "error".equalsIgnoreCase(status);
    }
}
