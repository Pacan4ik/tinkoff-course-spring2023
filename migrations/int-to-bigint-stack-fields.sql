alter table stack
    drop column answer_count;
alter table stack
    drop column comment_count;

alter table stack
    add column answer_count bigint not null default 0 check (answer_count >= 0);

alter table stack
    add column comment_count bigint not null default 0 check (comment_count >= 0);
