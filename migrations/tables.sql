create table link
(
    id                bigserial primary key,
    url               text unique              not null,
    event_description text                              default 'Новое событие',

    updated_at        timestamp with time zone not null default CURRENT_TIMESTAMP,
    created_at        timestamp with time zone not null default CURRENT_TIMESTAMP,
    checked_at        timestamp with time zone not null default CURRENT_TIMESTAMP
);
create table chat
(
    id            bigint primary key,

    registered_at TIMESTAMP with TIME zone not null default CURRENT_TIMESTAMP
);
create table link_chat_assignment
(
    link_id bigint references link (id) on delete cascade not null,
    chat_id bigint references chat (id) on delete cascade not null,
    constraint unique_link_chat_assignment primary key (link_id, chat_id)
);
