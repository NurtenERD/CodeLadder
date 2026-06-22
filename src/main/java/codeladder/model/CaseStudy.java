package codeladder.model;

public class CaseStudy {
    private final String routeBlockTitle;
    private final String title;
    private final String description;

    public CaseStudy(String routeBlockTitle, String title, String description) {
        this.routeBlockTitle = routeBlockTitle;
        this.title = title;
        this.description = description;
    }

    public String getRouteBlockTitle() {
        return routeBlockTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
