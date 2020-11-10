package io.github.abdulwahabo.filebox.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private List<File> files;

    public List<File> getFiles() {
        return new ArrayList<>(files);
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
