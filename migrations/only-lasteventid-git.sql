alter table git drop column open_issues_count;
alter table git drop column pushed_at;
alter table git drop column updated_at;
alter table git add column
last_event_id bigint check ( last_event_id >= 0 ) default null;
