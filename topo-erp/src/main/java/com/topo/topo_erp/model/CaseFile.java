package com.topo.topo_erp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "case_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType; // DWG, DXF, PDF, XLSX, DOC, etc.

    private Long fileSize; // in bytes

    @Column(nullable = false)
    private String filePath; // Path where file is stored

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseEntity;

    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    // File type enum for validation
    public enum FileType {
        DWG("AutoCAD Drawing", ".dwg"),
        DXF("AutoCAD DXF", ".dxf"),
        PDF("PDF Document", ".pdf"),
        XLSX("Excel Spreadsheet", ".xlsx"),
        XLS("Excel 97-2003", ".xls"),
        DOCX("Word Document", ".docx"),
        JPG("JPEG Image", ".jpg"),
        PNG("PNG Image", ".png"),
        TXT("Text File", ".txt");

        private final String description;
        private final String extension;

        FileType(String description, String extension) {
            this.description = description;
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }

        public String getDescription() {
            return description;
        }

        public static boolean isValid(String fileType) {
            for (FileType type : values()) {
                if (type.name().equalsIgnoreCase(fileType)) {
                    return true;
                }
            }
            return false;
        }
    }
}