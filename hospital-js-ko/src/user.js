import Panaceajs from '@medibloc/panacea-js';
import { BLOCKCHAIN_URL } from 'blockchain';

const panaceajs = Panaceajs.init([BLOCKCHAIN_URL]);

class User {
  constructor() {
    this.MNEMONIC = 'rate knife faculty sting still festival village between base disease violin device';
    this.PRIVATE_KEY = '6957772055e3f3587db5cbb5802dc67d8aa4bef5335ab4ee61ff7f5601fc89a7';
    this.ENCRYPTED_PRIVATE_KEY = {
      version: 3,
      id: '9cba103c-a7ee-4b51-8af7-02a0445acef6',
      crypto: {
        cipher: 'aes-128-ctr',
        ciphertext: '1c920b8d7b1563288e6acce71481fe288066861e76f607b1e7870b74bed682f6',
        cipherparams: {
          iv: 'ab1e15b7ddc04f9f6f0fd86cc03a26e6',
        },
        kdf: 'scrypt',
        kdfparams: {
          dklen: 32,
          n: 8192,
          p: 1,
          r: 8,
          salt: '5dd02c49779d07a8a7a0872274b3794532deba4434f65d9725e2d4f1c3de3872',
        },
        mac: 'eebcf4dd6ccfe95c743ca28f007371dc02679c0d9fbd2275fafdee0bb7c0d4ec',
      },
      address: '03107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507',
    };
    // address: 03107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507
    this.PUBLIC_KEY = '107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507933890e0c8f931351a9a37d7a151d1e8d9620b55adbe7a5e8663a4cea843f887';
    this.PASSWORD = 'userPassWord123!';

    this.userName = '홍길동';
    this.residentRegistrationNumber = '750101-1234567';

    this.account = new panaceajs.local.Account(
      this.PASSWORD,
      this.ENCRYPTED_PRIVATE_KEY,
      this.ENCRYPTED_PRIVATE_KEY.address,
    );

    console.log(`사용자 - 초기화를 완료 하였습니다. Blockchain address: ${this.account.pubKey}`);
  }

  getAddress() {
    return this.account.pubKey;
  }

  certify() {
    return {
      certificationResult: 'success',
      personName: this.userName,
      personBirthday: '19750101',
      personGender: '1',
      personNation: '0',
      personCi: '468w78a7v6ae9a',
      personMobileCompany: 'ABC',
      personMobileNumber: '01012345678',
    };
  }

  /**
   * 병원에 로그인 합니다.
   */
  signIn(hospital) {
    // 병원에서 nonce 를 전달받아 개인키로 서명합니다.
    const nonce = hospital.getSignInNonce(this.account.pubKey);
    const signature = panaceajs.cryptography.sign(this.PRIVATE_KEY, nonce);

    // 서명값을 병원에 전달하여 로그인을 완료하고 token 을 전달 받습니다.
    this.token = hospital.getSignInToken(this.account.pubKey, signature);
  }

  getToken() {
    return this.token;
  }
}

export { User as default };
