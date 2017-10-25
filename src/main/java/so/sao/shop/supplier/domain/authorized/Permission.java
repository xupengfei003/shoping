package so.sao.shop.supplier.domain.authorized;

/**
 * 权限实体
 *
 * @author
 * @create 2017-07-08 21:33
 **/
public class Permission {

	private Long id;
	/**
	 * 权限码
	 */
	private Integer code;
	/**
	 * 权限名称
	 */
	private String name;
	/**
	 * 权限类型【1、后台接口,2、菜单,3、按钮】
	 */
	private Integer type;
	/**
	 * 访问路径
	 */
	private String url;
	/**
	 * 权限描述
	 */
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
