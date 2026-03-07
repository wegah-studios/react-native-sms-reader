import { NativeModules, PermissionsAndroid, Platform } from "react-native";

const { SmsReader } = NativeModules;

function ensureAndroid() {
  if (Platform.OS !== "android") {
    throw new Error("sms-reader works only on Android");
  }
}

export async function requestSmsPermission() {
  if (Platform.OS !== "android") return true;

  const result = await PermissionsAndroid.request(
    PermissionsAndroid.PERMISSIONS.READ_SMS
  );

  return result === PermissionsAndroid.RESULTS.GRANTED;
}

export async function fetchMessages({
  startDate = null,
  endDate = null,
  sender = null,
  fetchCondition = null,
  lastId = null,
  limit = null,
}) {
  ensureAndroid();

  return SmsReader.fetchMessages(
    startDate ? startDate.getTime() : null,
    endDate ? endDate.getTime() : null,
    sender,
    fetchCondition,
    lastId,
    limit
  );
}

export default {
  requestSmsPermission,
  fetchMessages,
};