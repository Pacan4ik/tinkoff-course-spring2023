create table git
(
    id                bigint references link (id) on delete cascade primary key,
    open_issues_count int not null default 0 check ( open_issues_count >= 0 ),
    pushed_at         timestamp with time zone,
    updated_at        timestamp with time zone
);

create table stack
(
    id                 bigint references link (id) on delete cascade primary key,
    answer_count       int not null default 0 check ( answer_count >= 0 ),
    comment_count      int not null default 0 check ( comment_count >= 0 ),
    last_activity_date timestamp with time zone
);

-- noinspection SqlResolve
insert into git (id, open_issues_count, pushed_at, updated_at)
select id,
       (additional_info ->> 'open_issues_count')::int,
       (additional_info ->> 'pushed_at')::timestamp with time zone,
       (additional_info ->> 'updated_at')::timestamp with time zone
from link
where url like '%github.com%';

-- noinspection SqlResolve
insert into stack (id, answer_count, comment_count, last_activity_date)
select id,
       (additional_info ->> 'answer_count')::int,
       (additional_info ->> 'comment_count')::int,
       (additional_info ->> 'last_activity_date')::timestamp with time zone
from link
where url like '%stackoverflow%';

-- noinspection SqlResolve
alter table link drop column additional_info;

