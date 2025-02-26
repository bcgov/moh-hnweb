export const OUTPUT_DATE_FORMAT = 'YYYYMMDD'

export const INPUT_DATE_FORMAT = 'yyyyMMdd'

export const API_DATE_FORMAT = 'YYYY-MM-DD'

export const API_DATE_TIME_FORMAT = 'YYYY-MM-DDTHH:mm:ss'

export const DEFAULT_ERROR_MESSAGE = 'Please correct errors before submitting'

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
  { text: 'British Columbia', value: 'BC' },
  { text: 'Manitoba', value: 'MB' },
  { text: 'New Brunswick', value: 'NB' },
  { text: 'Newfoundland', value: 'NL' },
  { text: 'Northwest Territories', value: 'NT' },
  { text: 'Nova Scotia', value: 'NS' },
  { text: 'Nunavut', value: 'NU' },
  { text: 'Ontario', value: 'ON' },
  { text: 'P.E.I', value: 'PE' },
  { text: 'Quebec', value: 'QC' },
  { text: 'Saskatchewan', value: 'SK' },
  { text: 'Yukon', value: 'YT' },
  { text: 'Other Country', value: 'OC' },
  { text: 'U.S.A', value: 'US' },
]

// Dependent Relationships
export const RELATIONSHIPS = [
  { text: 'Select', value: '' },
  { text: 'Spouse', value: 'S' },
  { text: 'Dependent', value: 'D' },
]

// Genders
export const GENDERS = [
  { text: 'M', value: 'M' },
  { text: 'F', value: 'F' },
  { text: 'U', value: 'U' },
]

// Yes/No
export const YES_NO_OPTIONS = [
  { text: 'Yes', value: 'Y' },
  { text: 'No', value: 'N' },
]
