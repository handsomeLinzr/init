# init
由于公司产品涉及了B端和PC端的不同端点，而项目中同用一个权限服务，但是没有将B端权限和PC端的权限分离开，
最近有个需求，要求将B端和PC端的权限分离开，并且需要不影响已有角色的权限，故需要在分离权限后对旧角色权
限进行初始化。  
全部角色一共10w+，权限100w+，所以设计了这个项目利用接口调用的方式，用多线程是实现方式来实现角色权限的
初始化