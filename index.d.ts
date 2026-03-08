declare module "react-native-sms-reader" {
  export type SmsMessage = {
    id: number;
    body: string;
    sender: string;
    date: number;
  };

  export function requestSmsPermission(): Promise<boolean>;

  export function fetchSmsMessages(
    startDate?: Date,
    endDate?: Date,
    sender?: string,
    fetchCondition?: string,
    lastId?: number,
    limit?: number,
  ): Promise<SmsMessage[]>;

  const SmsReader: {
    fetchSmsMessages: typeof fetchSmsMessages;
    requestSmsPermission: typeof requestSmsPermission;
  };

  export default SmsReader;
}
