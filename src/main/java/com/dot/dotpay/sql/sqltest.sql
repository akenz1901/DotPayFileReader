-- (1) Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.

--     Ex: Write SQL to find IPs that made more than 100 requests starting from 2017-01-01.13:00:00 to 2017-01-01.14:00:00.
SELECT ip, count(ip) as ipTotalCount from onePipeLogDb
where start_date BETWEEN '2017-01-01 13:00:00' and '2017-01-01 14:00:00'
group by ip
having ipTotalCount > 100;


-- (2) Write MySQL query to find requests made by a given IP.
SELECT * from onePipeLogDb
where ip = '192.168.102.136';