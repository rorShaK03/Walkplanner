CREATE FUNCTION calculate_rating(total_rating INTEGER, total_votes INTEGER)
RETURNS FLOAT AS $$
BEGIN
    RETURN total_rating / total_votes;
END;
$$ LANGUAGE plpgsql;