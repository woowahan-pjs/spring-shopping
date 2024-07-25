create table if not exists product (
                         prdct_sn bigint auto_increment primary key comment '상품 일련번호',
                         prdct_id varchar(20) null comment '상품 아이디',
                         prdct_nm varchar(45) not null unique comment '상품 명',
                         price decimal(18,0) default 0  comment '가격',
                         image varchar(2000) null comment '상품 이미지',
                         del_yn enum('Y', 'N') not null default 'N' comment '삭제 여부',
                         reg_dt datetime not null comment '등록 일시',
                         mod_dt datetime null comment '수정 일시'
)
    comment '상품';

create table if not exists member (
                        mbr_sn bigint auto_increment primary key comment '회원 일련번호',
                        email varchar(200) unique not null comment '회원 이메일',
                        password varchar(1000) not null comment '회원 비밀번호',
                        mbr_nm varchar(100) null comment '회원 명',
                        nick_nm varchar(200) null comment '닉네임',
                        del_yn enum('Y', 'N') default 'N' not null comment '탈퇴 여부',
                        reg_dt datetime not null comment '등록 일시',
                        mod_dt datetime null comment '수정 일시'
)
    comment '회원';

create table if not exists token (
                       token_sn bigint auto_increment primary key comment '토큰 일련번호',
                       mbr_sn bigint not null comment '회원 일련번호',
                       token varchar(2000) not null comment '토큰',
                       expire_dt datetime not null comment '만료 일시',
                       reg_dt datetime not null comment '등록 일시',
                       mod_dt datetime null comment '수정 일시'
)
    comment '토큰';

create table if not exists wish (
                           wish_sn bigint auto_increment primary key comment '위시 일련번호',
                           mbr_sn bigint not null comment '회원 일련번호',
                           prdct_sn bigint not null comment '상품 일련번호',
                           cnt bigint not null default 1 comment '수량',
                           reg_dt datetime not null comment '등록 일시',
                           mod_dt datetime null comment '수정 일시'
)
    comment '위시 리스트';

create table if not exists slang (
                        slang_sn bigint auto_increment primary key comment '비속어 일련번호',
                        slang varchar(45) not null comment '비속어',
                        purgo_malum_yn enum('Y', 'N') not null comment 'purgoMalum 여부',
                        reg_dt datetime not null comment '등록 일시',
                        mod_dt datetime null comment '수정 일시'
)
    comment '비속어';
