package com.backend.healthcare_services.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileDB {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id; // automatically generated as UUID

    private String name; // name of the file

    private String type; // mime type

    @JsonIgnore
    @Lob // datatype for storing large object data
    private byte[] data; // array of bytes, map to a BLOB (BLOB is for storing binary data)

    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public Set<FileDB> setFile(FileDB file) {
        Set<FileDB> fileDBS = new HashSet<>();
        fileDBS.add(file);
        return fileDBS;
    }
}
