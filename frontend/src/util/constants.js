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

// State options
export const STATES = [
  { text: 'Select', value: '' },
  { text: 'Alabama', value: 'AL' },
  { text: 'Alaska', value: 'AK' },
  { text: 'Arizona', value: 'AZ' },
  { text: 'Arkansas', value: 'AR' },
  { text: 'California', value: 'CA' },
  { text: 'Colorado', value: 'CO' },
  { text: 'Connecticut', value: 'CT' },
  { text: 'Delaware', value: 'DE' },
  { text: 'Florida', value: 'FL' },
  { text: 'Georgia', value: 'GA' },
  { text: 'Hawaii', value: 'HI' },
  { text: 'Idaho', value: 'ID' },
  { text: 'Illinois', value: 'IL' },
  { text: 'Indiana', value: 'IN' },
  { text: 'Iowa', value: 'IA' },
  { text: 'Kansas', value: 'KS' },
  { text: 'Kentucky', value: 'KY' },
  { text: 'Louisiana', value: 'LA' },
  { text: 'Maine', value: 'ME' },
  { text: 'Maryland', value: 'MD' },
  { text: 'Massachusetts', value: 'MA' },
  { text: 'Michigan', value: 'MI' },
  { text: 'Minnesota', value: 'MN' },
  { text: 'Mississippi', value: 'MS' },
  { text: 'Missouri', value: 'MO' },
  { text: 'Montana', value: 'MT' },
  { text: 'Nebraska', value: 'NE' },
  { text: 'Nevada', value: 'NV' },
  { text: 'New Hampshire', value: 'NH' },
  { text: 'New Jersey', value: 'NJ' },
  { text: 'New Mexico', value: 'NM' },
  { text: 'New York', value: 'NY' },
  { text: 'North Carolina', value: 'NC' },
  { text: 'North Dakota', value: 'ND' },
  { text: 'Ohio', value: 'OH' },
  { text: 'Oklahoma', value: 'OK' },
  { text: 'Oregon', value: 'OR' },
  { text: 'Pennsylvania', value: 'PA' },
  { text: 'Rhode Island', value: 'RI' },
  { text: 'South Carolina', value: 'SC' },
  { text: 'South Dakota', value: 'SD' },
  { text: 'Tennessee', value: 'TN' },
  { text: 'Texas', value: 'TX' },
  { text: 'Utah', value: 'UT' },
  { text: 'Vermont', value: 'VT' },
  { text: 'Virginia', value: 'VA' },
  { text: 'Washington', value: 'WA' },
  { text: 'West Virginia', value: 'WV' },
  { text: 'Wisconsin', value: 'WI' },
  { text: 'Wyoming', value: 'WY' },
]

// Dependent Relationships
export const RELATIONSHIPS = [
  { text: 'Select', value: '' },
  { text: 'Spouse', value: 'S' },
  { text: 'Dependent', value: 'D' },
]

// COUNTRY OPTIONS
export const COUNTRIES = [
  { text: 'Canada', value: 'Canada' },
  { text: 'United States', value: 'United States' },
  { text: 'Other', value: 'Other' },
]

// Genders
export const GENDERS = [
  { text: 'Male', value: 'M' },
  { text: 'Female', value: 'F' },
  { text: 'Unknown', value: 'U' },
]

// Yes/No
export const YES_NO_OPTIONS = [
  { text: 'Yes', value: 'Y' },
  { text: 'No', value: 'N' },
]
