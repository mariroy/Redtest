package data.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Post extends BaseModel {
    private String positionName;
    private BigDecimal salary;

    public Post(Long id) {
        super(id);
    }

    public Post() {
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(positionName, post.positionName) && Objects.equals(salary, post.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionName, salary);
    }

    @Override
    public String toString() {
        return "Post{" +
                "positionName='" + positionName + '\'' +
                ", salary=" + salary +
                '}';
    }
}
