import { BLOCKCHAIN_URL, ACCOUNT_REQUEST_TYPE_TAIL, CHAIN_ID } from 'blockchain'
import Medjs from 'medjs';
import { certificateDataV1Utils } from 'phr-js';

const medjs = Medjs.init([BLOCKCHAIN_URL]);

class MediBloc {
  constructor() {
    this.MNEMONIC = 'slam wool bulk fine reduce honey capital wheat evoke enjoy treat flip';
    this.PRIVATE_KEY = '4da8bc28a095870433d8a7d57ca140d6132e722f177c9a94f70a6963b4b8f708';
    this.ENCRYPTED_PRIVATE_KEY = {
      version: 3,
      id: 'ecfcea6e-bd80-48e8-aefb-c62974665451',
      crypto: {
        cipher: 'aes-128-ctr',
        ciphertext: 'fe380d13659a4289f2512fb2650fe996fd85f01fbfe022d668bcc64cb3695c9a',
        cipherparams: {
          iv: '4c13d9f9d0c14f667bd7f9d00e4a4a24',
        },
        kdf: 'scrypt',
        kdfparams: {
          dklen: 32,
          n: 8192,
          p: 1,
          r: 8,
          salt: '16212236e086387a84028366f02cd9e07f10b427a3de5e916ae00fd38bc27a7e',
        },
        mac: '1d8a97e0f9fa248130cb13789abd57a18266335c5fa175ae8070e7ae03170cbb',
      },
      address: '02e34caca7b7653eb6cbb64cdd9e7c691545cbbe002a5ef9ed86e71577d9c7c296',
    };
    // address: 02e34caca7b7653eb6cbb64cdd9e7c691545cbbe002a5ef9ed86e71577d9c7c296
    this.PUBLIC_KEY = 'e34caca7b7653eb6cbb64cdd9e7c691545cbbe002a5ef9ed86e71577d9c7c2960da413ededc3216df47f27ba6d46babe0ba54ca35d682182d26a6c6aa63f7930';
    this.PASSWORD = 'MediBlocPassWord123!';

    this.account = new medjs.local.Account(
      this.PASSWORD,
      this.ENCRYPTED_PRIVATE_KEY,
      this.ENCRYPTED_PRIVATE_KEY.address,
    );

    console.log(`MediBloc - 초기화를 완료 하였습니다. Blockchain address: ${this.account.pubKey}`);
  }

  static generateCertificate(blockchainAddress, certification) {
    const certificate = { blockchainAddress, certification };
    return certificateDataV1Utils.fillCertificate(certificate);
  }

  sendCertificate(certificate) {
    // Blockchain 에서 병원 account 의 현재 정보를 조회 합니다.
    return medjs.client.getAccount(this.account.pubKey, null, ACCOUNT_REQUEST_TYPE_TAIL)
      .then((accountStatus) => {
        const nonce = parseInt(accountStatus.nonce, 10);

        const certificateHash = certificateDataV1Utils.hashCertificate(certificate);
        const txPayload = medjs.local.transaction.createDataPayload(certificateHash);

        const tx = medjs.local.transaction.dataUploadTx({
          from: this.account.pubKey,
          payload: txPayload,
          nonce: nonce + 1,
          chain_id: CHAIN_ID,
        });

        // transaction 을 sign 합니다. 비밀번호는 병원 account 의 개인키를 복호화 하는 데 사용 됩니다.
        this.account.signTx(tx, this.PASSWORD);

        return medjs.client.sendTransaction(tx).then((txHash) => {
          return txHash;
        });
      });
  }
}

export { MediBloc as default}
