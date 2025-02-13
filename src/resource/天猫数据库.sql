
--分类表
drop table tmCategory;
create table tmCategory(
  cid number , --分类id
  name varchar2(500), --分类名称
  constraint pk_cate_id primary key (cid)
);
drop sequence tm_cid;
create sequence tm_cid increment by 1 start with 1001;--分类表序列（tm_cid）


--用户表
drop table tmuser;
create table tmuser(
       tmuid number,            --用户ID
       name varchar2(500),      --用户名
       password varchar2(500),          --密码
       constraint pk_user_tmuid primary key(tmuid)
);
drop sequence tm_tmuid;
create sequence tm_tmuid increment by 1 start with 8001;--评论表序列（tm_tmuid）

--商店管理员表
drop table tmstoreadmin;
create table tmstoreadmin(
       sdid number primary key,--商店管理员编号
       name varchar2(400) not null,--商店管理员姓名
       password varchar2(400) not null--商店管理员登陆密码
);
drop sequence tm_sdid;
create sequence tm_sdid increment by 1 start with 11001;--商店管理员表序列（tm_sdid）

--网站管理员表
drop table tmadmin;
create table tmadmin(
     aid number primary key,--网站管理员编号
     name varchar2(400) not null,--网站管理员姓名
     password varchar2(400) not null,--网站管理员登陆密码
     class varchar2(400) not null--网站管理员的级别
);
drop sequence tm_aid;
create sequence tm_aid increment by 1 start with 12001;--网站管理员表序列（tm_aid）

--属性表
drop table tmproperty;
create table tmproperty(
       pid number, --属性id
       cid number not null,--所属类id
       name varchar2(500), --属性名称
       constraint pk_prop_id primary key (pid),
       constraint tmpr_cid_fk foreign key(cid) references tmCategory(cid)--外键
);
drop sequence tm_pid;
create sequence tm_pid increment by 1 start with 2001;--分类表序列（tm_pid）


--商店表
drop table tmstore;
create table tmstore(
       sid number, --商店id
       sdid number, --商店管理员id
       name varchar2(500),--商店名称
       phone varchar2(500),--商店联系电话
       describe varchar2(500),--商店描述
       constraint pk_store_id primary key (sid),
       constraint tmsto_sdid_fk foreign key(sdid) references tmstoreadmin(sdid)--外键
);
drop sequence tm_sid;
create sequence tm_sid increment by 1 start with 5001;--商品属性值表序列（tm_sid）

--商品表
drop table tmproduct;
create table tmproduct(
       pdid number,  --商品id
       name varchar2(500), --商品名称
       subtitle varchar2(500), --商品副标题（简述）
       originprice number, --商品原价
       promoteprice number, --商品促销价
       cid number, --商品类别id
       sid number, --店铺id
       constraint pk_prod_id primary key (pdid),
       constraint tmprd_cid_fk foreign key(cid) references tmCategory(cid),--外键
       constraint tmpr_spid_fk foreign key(sid) references tmstore(sid)--外键
);
drop sequence tm_pdid;
create sequence tm_pdid increment by 1 start with 3001;--商品表序列（tm_pdid）

--商店仓库表
drop table tmstorewarehouse;
create table tmstorewarehouse(
       spid number primary key,--商店仓库id
       sid number not null,--商店编号
       pdid number not null,--产品编号
       count number not null,--产品数量 
       constraint tmst_sid_fk foreign key(sid) references tmstore(sid),--外键
       constraint tmst_pdid_fk foreign key(pdid) references tmProduct(pdid)--外键      
);
drop sequence tm_spid;
create sequence tm_spid increment by 1 start with 15001;--商店仓库表序列（tm_spid）


--商品属性值表
drop table tmpropertyvalue;
create table tmpropertyvalue(
       pvid number, --属性值id
       pid number, --所属属性id
       pdid number, --所属商品id
       value varchar2(500), --属性值
       constraint pk_propval_id primary key(pvid),
       constraint tmpro_pid_fk foreign key(pid) references tmProperty(pid),--外键
       constraint tmpro_pdid_fk foreign key(pdid) references tmProduct(pdid)--外键
);
drop sequence tm_pvid;
create sequence tm_pvid increment by 1 start with 4001;--商品属性值表序列（tm_pvid）


--产品图片表
drop table tmproductimage;
create table tmproductimage(
       piid number,   --产品图片id
       pdid number,    --属性id
       imageadd varchar2(500),    --图片路径
       constraint pk_productimg_piid primary key(piid),
       constraint tmpr_pdid_fk foreign key(pdid) references tmProduct(pdid)--外键 
);
drop sequence tm_piid;
create sequence tm_piid increment by 1 start with 6001;--产品图片表序列（tm_piid）

--评论表
drop table tmreview;
create table tmreview(
       rid number,     --评论id
       tmuid number,     --用户id
       pdid number,      --产品id
       content varchar2(500),        --评论
       createdate date,              --评论时间
       constraint pk_review_rid primary key(rid),
       constraint tmre_tmuid_fk foreign key(tmuid) references tmUser(tmuid),--外键 
       constraint tmre_pdid_fk foreign key(pdid) references tmProduct(pdid)--外键
);
drop sequence tm_rid;
create sequence tm_rid increment by 1 start with 7001;--评论表序列（tm_rid）


--订单表
drop table tmorder;
create table tmorder(
       oid number,              --订单id  
       ordercode number,        --订单号
       address varchar2(500),   --地址
       post number,             --邮编
       recevier varchar2(500),  --接受者
       phone varchar2(500),     --电话
       usermassage varchar2(500),       --备注消息
       createdate date,                 --创建时间
       paydate date,                    --支付时间
       deliverydate date,               --发货时间
       confirmdate date,                --完成时间
       status number,                   --状态
       tmuid number,                    --用户id
       constraint pk_order_oid primary key(oid),
       constraint tmor_tmuid_fk foreign key(tmuid) references tmUser(tmuid)--外键
);
drop sequence tm_oid;
create sequence tm_oid increment by 1 start with 9001;--订单表序列（tm_oid）

--订单项表
drop table tmorderitem;
create table tmorderitem(
       oiid number,            --订单项id
       pdid number,            --产品id
       tmuid number,           --用户id
       oid number,             --订单id
       count number,           --数量
       constraint pk_orderitem_oiid primary key(oiid),
       constraint tmorder_pdid_fk foreign key(pdid) references tmProduct(pdid),--外键
       constraint tmorder_tmuid_fk foreign key(tmuid) references tmUser(tmuid),--外键
       constraint tmor_oid_fk foreign key(oid) references tmOrder(oid)--外键
);
drop sequence tm_oiid;
create sequence tm_oiid increment by 1 start with 10001;--订单项表序列（tm_oiid）

--收货地址表
drop table tmshoppingAddress;
create table tmshoppingAddress(
       addid number primary key,--收货地址编号
       tmuid number not null,--用户编号
       address varchar2(400) not null,--收货地址
       post varchar2(400) not null,--邮编
       recevier varchar2(400),--收货人
       phone varchar2(400) not null,--联系电话
       constraint tmsh_addid_fk foreign key(tmuid) references tmUser(tmuid)--外键
);
drop sequence tm_addid;
create sequence tm_addid increment by 1 start with 13001;--收货地址表序列（tm_addid）

--个人信息表
drop table tmpersonalInformation;
create table tmpersonalInformation(
       perid number primary key,--个人详细信息编号
       tmuid number not null,--用户编号
       name varchar2(400) not null,--用户昵称
       headimg varchar2(400) not null,--用户头像
       sex varchar2(400) not null,--用户性别
       signature varchar2(400) not null,--个性签名
       constraint tmpe_perid_fk foreign key(tmuid) references tmUser(tmuid)--外键
);
drop sequence tm_perid;
create sequence tm_perid increment by 1 start with 14001;--个人信息表序列（tm_perid）

