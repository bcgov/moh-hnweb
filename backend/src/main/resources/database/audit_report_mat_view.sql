
-- Drop existing view
DROP MATERIALIZED VIEW mspdirect.organization;

-- Create new materialized view
CREATE MATERIALIZED VIEW mspdirect.organization 
AS
select DISTINCT
    t.organization as organization
from 
    mspdirect.transaction t
where organization is not null
WITH DATA;

-- Refresh 
REFRESH MATERIALIZED VIEW mspdirect.organization;
