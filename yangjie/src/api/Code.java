package api;

public interface Code {
    /**
     *
             当GET, PUT和PATCH请求成功时，要返回对应的数据，及状态码200，即SUCCESS
             当POST创建数据成功时，要返回创建的数据，及状态码201，即CREATED
             当DELETE删除数据成功时，不返回数据，状态码要返回204，即NO CONTENT
             当GET 不到数据时，状态码要返回404，即NOT FOUND
             任何时候，如果请求有问题，如校验请求数据时发现错误，要返回状态码 400，即BAD REQUEST
             当API 请求需要用户认证时，如果request中的认证信息不正确，要返回状态码 401，即NOT AUTHORIZED
             当API 请求需要验证用户权限时，如果当前用户无相应权限，要返回状态码 403，即FORBIDDEN
             */
             String SUCCESS = "200";
             String FAILURE = "-1";

             // 下面的状态码暂不启用！
             String CREATE = "201";
             String DELETE = "204";
             String NOT_FOUND = "404";
             String BAD_REQUEST = "400";
             String NOT_AUTHORIZED = "401";
             String FORBIDDEN = "403";
             }
