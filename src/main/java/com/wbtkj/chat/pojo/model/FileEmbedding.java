package com.wbtkj.chat.pojo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class FileEmbedding implements Serializable {
    private Long id;

    private String name;

    private String text;

    private List<BigDecimal> embedding;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public List<BigDecimal> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<BigDecimal> embedding) {
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", text=").append(text);
        sb.append(", embedding=").append(embedding);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}