
-- Drop existing view
DROP MATERIALIZED VIEW mspdirect.organization;

-- Create new materilaized view
CREATE MATERIALIZED VIEW mspdirect.organization 
AS
select DISTINCT
    t.organization as organization
from 
    mspdirect.transaction t  
WITH DATA

-- Refresh 
REFRESH MATERIALIZED VIEW mspdirect.organization;
