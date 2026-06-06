package ru.itis.flavorful_book.api.dto;

public class ExternalMealDTO {
    private String id;
    private String name;
    private String category;
    private String area;
    private String instructions;
    private String thumbnailUrl;
    private String youtubeUrl;
    private String sourceUrl;

    public ExternalMealDTO() {}

    public ExternalMealDTO(String id, String name, String category, String area,
                           String instructions, String thumbnailUrl,
                           String youtubeUrl, String sourceUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.thumbnailUrl = thumbnailUrl;
        this.youtubeUrl = youtubeUrl;
        this.sourceUrl = sourceUrl;
    }

    public String getId()           { return id; }
    public String getName()         { return name; }
    public String getCategory()     { return category; }
    public String getArea()         { return area; }
    public String getInstructions() { return instructions; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getYoutubeUrl()   { return youtubeUrl; }
    public String getSourceUrl()    { return sourceUrl; }
}
