package com.zaclippard.androidworkshopapp.models;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;

public class School {
    private final List<Classroom> classrooms;

    private final Principal principal = new Principal(
            "Some Principal",
            50,
            Collections.singletonList(new Credential("Credential1", "Accreditation"))
    );

    public School(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    @NonNull
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Classroom c : this.classrooms) {
            s.append(c.toString()).append("\n");
        }
        return s.toString();
    }

    public int getClassroomCount() {
        return this.classrooms.size();
    }

    public String getClassroomTag() {
        return Classroom.TAG;
    }

    public int getLibraryBookCount() {
        return Library.INSTANCE.getBooks().size();
    }

    public List<Credential> getPrincipalCredentials() {
        return this.principal.getCredentials();
    }
}
