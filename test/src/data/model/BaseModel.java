package data.model;

public abstract class BaseModel implements Model {
    private Long id;

    public BaseModel(Long id) {
        this.id = id;
    }

    public BaseModel() {
    }

    @Override
    public Long getId() {
        return id;
    }
}
