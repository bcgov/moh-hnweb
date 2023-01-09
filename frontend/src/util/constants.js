export const OUTPUT_DATE_FORMAT = 'YYYYMMDD'

export const INPUT_DATE_FORMAT = 'yyyyMMdd'

export const API_DATE_FORMAT = 'YYYY-MM-DD'

export const API_DATE_TIME_FORMAT = 'YYYY-MM-DDTHH:mm:ss'

export const DEFAULT_ERROR_MESSAGE = 'Please correct errors before submitting'

const HCIM_CONTACT_NO = config.HCIM_CONTACT_NO || import.meta.env.VITE_HCIM_CONTACT_NO

export const DATE_OF_DEATH_MESSAGE = 'A Date of Death was found for this client record retrieved from HCIM.  Please contact HCIM at ' + HCIM_CONTACT_NO + ' (8am to 4:30pm, Mon - Fri) to confirm whether or not this individual is deceased before proceeding with the study permit application.'

export const COVERAGE_END_REASONS = new Map([
  ['OO', 'OPTED OUT'],
  ['OOPM', 'OUT OF PROVINCE MOVE'],
  ['LOSC', 'NO CONTACT WITH MSP'],
  ['RESQ', 'RESIDENCY IN QUESTION'],
  ['DEAD', 'DECEASED'],
  ['CHAR', 'EXCLUDED - RCMP'],
  ['CHAF', 'EXCLUDED - ARMED FORCES'],
  ['CHAP', 'EXCLUDED - FEDERAL INSTITUTION'],
  ['ADMN', 'ADMINISTRATIVE'],
])

// Province options
export const PROVINCES = [
  { text: 'Select', value: '' },
  { text: 'Alberta', value: 'AB' },
  { text: 'British Columbia', value: 'BC' },
  { text: 'Manitoba', value: 'MB' },
  { text: 'New Brunswick', value: 'NB' },
  { text: 'Newfoundland', value: 'NL' },
  { text: 'Nova Scotia', value: 'NS' },
  { text: 'Northwest Territories', value: 'NT' },
  { text: 'Nunavut', value: 'NU' },
  { text: 'Ontario', value: 'ON' },
  { text: 'P.E.I', value: 'PE' },
  { text: 'Quebec', value: 'QC' },
  { text: 'Saskatchewan', value: 'SK' },
  { text: 'Yukon', value: 'YT' },
]

// Immigration Code options
export const IMMIGRATION_CODES = [
  { text: 'Select', value: '' },
  { text: 'Student Authorization', value: 'S' },
  { text: 'Employment Authorization', value: 'W' },
]

// Prior Residence drop down options
export const PRIOR_RESIDENCES = [
  { text: 'Select', value: '' },
  { text: 'Alberta', value: 'AB' },
  { text: 'Manitoba', value: 'MB' },
  { text: 'New Brunswick', value: 'NB' },
  { text: 'Newfoundland', value: 'NL' },
  { text: 'Nova Scotia', value: 'NS' },
  { text: 'Northwest Territories', value: 'NT' },
  { text: 'Nunavut', value: 'NU' },
  { text: 'Other Country', value: 'OC' },
  { text: 'Ontario', value: 'ON' },
  { text: 'P.E.I', value: 'PE' },
  { text: 'Quebec', value: 'QC' },
  { text: 'Saskatchewan', value: 'SK' },
  { text: 'U.S.A', value: 'US' },
  { text: 'Yukon', value: 'YT' },
  { text: 'British Columbia', value: 'BC' },
]

// Dependent Relationships
export const RELATIONSHIPS = [
  { text: 'Select', value: '' },
  { text: 'Spouse', value: 'S' },
  { text: 'Dependent', value: 'D' },
]
