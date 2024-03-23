CREATE OR REPLACE FUNCTION calculate_distance(lat1 float, lon1 float, lat2 float, lon2 float) RETURNS float AS $$
DECLARE
    radius float := 6371; -- средний радиус Земли в км
    d_lat float := radians(lat2 - lat1);
    d_lon float := radians(lon2 - lon1);
    a float;
    c float;
BEGIN
    a := sin(d_lat / 2) * sin(d_lat / 2) + cos(radians(lat1)) * cos(radians(lat2)) * sin(d_lon / 2) * sin(d_lon / 2);
    c := 2 * atan2(sqrt(a), sqrt(1 - a));
    RETURN radius * c;
END;
$$ LANGUAGE plpgsql;