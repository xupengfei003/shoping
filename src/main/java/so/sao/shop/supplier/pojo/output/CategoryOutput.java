package so.sao.shop.supplier.pojo.output;

public class CategoryOutput {

    /**
     * id
     */
    private long id;
    /**
     * 分类名称
     */
    private String name;

    /**
     * 类型级别
     */
    private int level;
    /**
     *  PID
     */
    private Long pid;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
