create table currencies(

    id int primary key generated always as identity,
    code varchar(3) unique not null,
    full_name varchar(64) not null,
    sign varchar(10) not null

    );

create table exchange_rates(

    id int primary key generated always as identity,
    base_currency_id int references currencies(id),
    target_currency_id int references currencies(id),
    rate decimal(6) not null,
    unique(base_currency_id, target_currency_id)

)