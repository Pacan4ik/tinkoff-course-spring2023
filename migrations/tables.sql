create table links
(
    id                bigserial primary key,
    url               text                     not null,
    event_description text                              default 'Новое событие',

    updated_at        timestamp with time zone not null default CURRENT_TIMESTAMP,
    created_at        timestamp with time zone not null default CURRENT_TIMESTAMP
);
create table chats
(
    id            bigint primary key,

    registered_at TIMESTAMP with TIME zone not null default CURRENT_TIMESTAMP
);
create table link_chat_assignment
(
    id      bigserial primary key,
    link_id bigint references links (id) on delete cascade not null,
    chat_id bigint references chats (id) on delete cascade not null
);
