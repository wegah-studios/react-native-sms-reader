declare module "react-native-sms-reader" {
  export type SmsMessage = {
    id: number;
    body: string;
    sender: string;
    date: number;
  };

  export type SmsSearchParams = {
    startDate?: Date;
    endDate?: Date;
    sender?: string;
    fetchCondition?: string;
    lastId?: number;
    limit?: number;
  };

  export function requestSmsPermission(): Promise<boolean>;

  export function fetchMessages(params: SmsSearchParams): Promise<SmsMessage[]>;

  const SmsReader: {
    fetchMessages: typeof fetchMessages;
    requestSmsPermission: typeof requestSmsPermission;
  };

  export default SmsReader;
}
