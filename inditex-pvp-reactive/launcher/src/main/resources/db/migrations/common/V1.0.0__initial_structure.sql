create table if not exists price
(
    id            integer generated always as identity constraint price_pk primary key,
    brand_id      integer not null,
    start_date    timestamp not null,
    end_date      timestamp not null,
    price_list    integer not null,
    product_id    integer not null,
    priority      integer not null,
    price         numeric(20,2) not null,
    currency      varchar(3)
);

create index if not exists price_product_index on price (product_id);


