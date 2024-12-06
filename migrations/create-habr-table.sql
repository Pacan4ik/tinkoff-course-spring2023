create table habr (
    id bigint references link (id) on delete cascade primary key,
    comment_count bigint not null default 0 check ( comment_count >= 0 ),
    content_sha1_hash varchar(40)
);
