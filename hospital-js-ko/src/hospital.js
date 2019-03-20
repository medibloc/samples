import { BLOCKCHAIN_URL, ACCOUNT_REQUEST_TYPE_TAIL, CHAIN_ID } from 'blockchain';
import Panaceajs from '@medibloc/panacea-js';
import { certificateDataV1Utils, claimDataV1Utils } from '@medibloc/phr-js';
import jwt from 'jsonwebtoken';

const panaceajs = Panaceajs.init([BLOCKCHAIN_URL]);

class Hospital {
  /**
   * 테스트 실행을 위해 샘플 데이터를 설정 하고, 블록체인 account 를 생성 합니다.
   */
  constructor() {
    /** 샘플 데이터 설정 */
    this.MNEMONIC = 'canyon roast street knock library amount enter popular sea kidney pupil furnace';
    this.PRIVATE_KEY = 'eede9347908b2ac3801828cc3293da19109c0730c47314a694c9acacbb95d3da';
    this.ENCRYPTED_PRIVATE_KEY = {
      version: 3,
      id: '267c542a-8233-4097-955d-9c8c3d72ac59',
      crypto: {
        cipher: 'aes-128-ctr',
        ciphertext: '7ff33b594cdb5911fca22ee2bb2ece9b81b980c67151a765b5e997654c8ea91c',
        cipherparams: {
          iv: 'd4b19bb8893fb6246c05e7dd1c23d3e8',
        },
        kdf: 'scrypt',
        kdfparams: {
          dklen: 32,
          n: 8192,
          p: 1,
          r: 8,
          salt: '368a05477cf1c0905d8454e71c766a707c9377cdd328f2ee64290eba245614f3',
        },
        mac: 'b9c3440fa34e76d4e92d0b15e6ac0af77a597cfb8e7510a9a702ff0d8a146025',
      },
      address: '02718101c8a565a58bf416c8d30b335e6bb9701d1532e76b38298ef7e252c321cd',
    };
    // address: 02718101c8a565a58bf416c8d30b335e6bb9701d1532e76b38298ef7e252c321cd
    this.PUBLIC_KEY = '718101c8a565a58bf416c8d30b335e6bb9701d1532e76b38298ef7e252c321cd3077389f0517d40faac6d8db45aa81ad86914f995abcbdbdc6e9605a1e46c844';
    this.PASSWORD = 'hospitalPassWord123!';

    // 병원의 환자 list 생성
    this.patientList = [
      { no: '00000000', name: 'ㅇㅇㅇ', RRN: '000000-0000000' },
      { no: '11111111', name: 'ㅁㅁㅁ', RRN: '111111-1111111' },
      { no: '12345678', name: '홍길동', RRN: '750101-1234567' },
    ];

    /** 블록체인 account 생성 */
    // 새로운 account 를 생성합니다. 여기서는 예제 코드 작동을 위해 주어진 encrypted private key 를 이용 합니다.
    this.account = new panaceajs.local.Account(
      this.PASSWORD,
      this.ENCRYPTED_PRIVATE_KEY,
      this.ENCRYPTED_PRIVATE_KEY.address,
    );
    // 새로운 keyPair 를 생성 하고자 하는 경우, 다음 코드를 이용 합니다.
    // this.account = new Account();

    console.log(`병원 - 초기화를 완료 하였습니다. Blockchain address: ${this.account.pubKey}`);
  }

  /**
   * 주민등록번호로 환자 ID 를 조회하여, 해당 환자 정보에 블록체인 account 를 등록 합니다.
   * 실제 구현 시, 아래 제시된 예외처리가 모두 포함 되어야 합니다.
   */
  async mapAccountOntoPatientId(user) {
    // 인증서 블록체인 주소 일치 여부 확인
    if (user.getAddress() !== user.certificate.blockchainAddress) {
      throw new Error('주어진 블록체인 주소가 인증서의 블록체인 주소와 일치하지 않습니다.');
    }

    // tx 의 인증서 hash 와 일치 여부 확인
    const isUploaded
      = await Hospital.isUploadedOnBlockchain(user.certificate, user.certificateTxHash);
    if (!isUploaded) {
      throw new Error('주어진 인증서가 해당 transaction 에 기록 되어 있지 않습니다.');
    }

    // CI 유효성 확인
    const ci = user.certificate.certification.personCi;
    if (!Hospital.isValidCI(ci, user.residentRegistrationNumber)) {
      throw new Error('주어진 CI 는 해당 주민등록번호의 CI 가 아닙니다.');
    }

    // 환자 ID 와 블록체인 account 연계
    const patient = this.findPatientWithRRN(user.residentRegistrationNumber);
    if (patient != null) {
      patient.blockchainAddress = user.getAddress();
    } else {
      throw new Error(`주민등록번호가 ${user.residentRegistrationNumber} 인 환자 정보를 찾을 수 없습니다.`);
    }
  }

  /**
   * 로그인 시 환자가 사용할 nonce 값을 생성하여 반환 합니다.
   * 이후 환자가 nonce 값에 서명한 결과를 검증 하기 위해, 환자에게 전달한 nonce 값을 저장 합니다.
   */
  getSignInNonce(patientBlockchainAddress) {
    const patient = this.findPatientWithBlockchainAddress(patientBlockchainAddress);

    const nonce = panaceajs.utils.randomHex(32);
    patient.nonce = nonce;

    return nonce;
  }

  /**
   * 환자가 nonce 값에 서명한 결과를 검증 하고, 검증에 성공했다면 JWT(Json Web Token) 를 반환 합니다.
   * 환자는 이 함수를 호출하여 받은 JWT 를 이후 병원과 통신 시 사용 합니다.
   */
  getSignInToken(patientBlockchainAddress, signature) {
    const patient = this.findPatientWithBlockchainAddress(patientBlockchainAddress);

    const isValidSig = panaceajs.cryptography.verifySignature(
      patientBlockchainAddress,
      patient.nonce,
      signature,
    );
    if (isValidSig) {
      return jwt.sign({ bcAddress: patientBlockchainAddress }, this.PRIVATE_KEY);
    }

    throw new Error(`${patientBlockchainAddress} 의 nonce 값에 대한 signature 가 올바르지 않습니다.`);
  }

  /**
   * 주어진 블록체인 address 를 갖는 환자의 진료 청구서를 생성하여 반환 합니다.
   */
  getClaim(token) {
    let tokenData;
    try {
      tokenData = jwt.verify(token, this.PRIVATE_KEY);
    } catch (err) {
      throw new Error('유효한 토큰이 아닙니다.');
    }

    const patient = this.findPatientWithBlockchainAddress(tokenData.bcAddress);
    if (patient != null) {
      const sampleClaim = {
        patientNo: patient.patientNo,
        patientName: patient.patientName,
        receipts: [{
          receiptNo: '20181204-S1284',
          receiptType: 'I',
          patientNo: '12345678',
          patientName: '홍길동',
          treatmentStartDate: '2018-12-06',
          treatmentEndDate: '2018-12-06',
          treatmentDepartment: '피부과',
          treatmentDepartmentCode: 'DER',
          coveredFee: '11000',
          uncoveredFee: '20000',
          upperLimitExcess: '0',
          payTotal: '31000',
          patientPayTotal: '21000',
          deductAmount: '0',
          advancePayAmount: '0',
          payAmount: '21000',
          uncollectedPayAmount: '0',
          receiptAmount: '21000',
          surtaxAmount: '0',
          cashPayAmount: '0',
          cardPayAmount: '21000',
          feeItems: [{
            feeItemName: '초진 진찰료',
            treatmentDate: '2018-12-06',
            medicalChargeCode: 'AA157',
            price: '11000',
            quantity: '1',
            repeatNumber: '1',
            feeTotal: '11000',
            coveredPatientFee: '1000',
            coveredInsuranceFee: '10000',
            coveredPatientAllFee: '0',
            uncoveredChosenFee: '0',
            uncoveredUnchosenFee: '0',
          }, {
            feeItemName: '검사료',
            treatmentDate: '2018-12-06',
            medicalChargeCode: 'BB157',
            price: '20000',
            quantity: '1',
            repeatNumber: '1',
            feeTotal: '20000',
            coveredPatientFee: '0',
            coveredInsuranceFee: '0',
            coveredPatientAllFee: '0',
            uncoveredChosenFee: '20000',
            uncoveredUnchosenFee: '0',
          }],
        }],
        prescriptions: [{
          givenNo: '301',
          patientName: patient.patientName,
          patientBirthdate: '19750101',
          patientGender: '1',
          diagnosisCode: 'DC001',
          doctorName: '김철수',
          doctorLicenseNo: '00000',
          prescriptionItems: [{
            drugCode: 'AA01',
            drugName: 'DrugName',
            dailyDose: '30mg',
            dailyFrequency: '3',
            prescriptionDuration: '5',
            usage: '용법',
          }],
        }],
      };

      return claimDataV1Utils.fillClaim(sampleClaim);
    }

    throw new Error(`${tokenData.bcAddress} 주소를 가진 환자 정보를 찾을 수 없습니다.`);
  }

  /**
   * 청구서를 병원의 개인키로 sign 하고, 블록체인에 기록 할 수 있는 transaction 형태로 반환 합니다.
   */
  async getSignedTransaction(claim) {
    // Blockchain 에서 병원 account 의 현재 정보를 조회 합니다.
    const accountStatus
      = await panaceajs.client.getAccount(this.account.pubKey, null, ACCOUNT_REQUEST_TYPE_TAIL);
    const nonce = parseInt(accountStatus.nonce, 10);

    // Blockchain 에 업로드 할 claim hash 값을 구하고, 블록체인 payload 에 기록 할 형태로 변환 합니다.
    const claimHash = claimDataV1Utils.hashClaim(claim);
    const txPayload = panaceajs.local.transaction.createDataPayload(claimHash);

    // Blockchain 에 기록 할 transaction 을 생성하고, hash 값을 추가합니다.
    const tx = panaceajs.local.transaction.dataUploadTx({
      from: this.account.pubKey,
      payload: txPayload,
      nonce: nonce + 1,
      chain_id: CHAIN_ID,
    });

    // transaction 을 sign 합니다. 비밀번호는 병원 account 의 개인키를 복호화 하는 데 사용 됩니다.
    this.account.signTx(tx, this.PASSWORD);

    // 생성한 transaction 을 반환 합니다.
    // 사용자는 이 transaction 을 블록체인에 등록 하여 이후 진본증명 시 이용 합니다.
    return tx;
  }

  /**
   * certificateTxHash 로 블록체인에서 transaction 을 조회 하고,
   * 조회 한 transaction 에 기록 된 hash 값과 주어진 인증서의 hash 값이 일치 하는 지 여부를 반환 합니다.
   */
  static isUploadedOnBlockchain(certificate, certificateTxHash) {
    // 주어진 인증서의 hash 깂
    const certificateHash = certificateDataV1Utils.hashCertificate(certificate);
    const certificateHashPayload = panaceajs.local.transaction.createDataPayload(certificateHash);

    try {
      // 블록체인에 기록 된 인증서 hash 값
      return panaceajs.client.getTransaction(certificateTxHash.hash)
        .then((tx) => {
          if (!tx) {
            throw new Error(`Can not find the transaction ${certificateTxHash}`);
          }

          if (!tx.payload) {
            throw new Error('Transaction payload is empty.');
          }

          // TODO : use protobuf-JSON converting instead of substring()
          return certificateHashPayload.hash.toString('hex') === tx.payload.substring(4, tx.payload.length);
        });
    } catch (err) {
      throw new Error(`Can not find the transaction ${certificateTxHash} ${err}`);
    }
  }

  /**
   * 인증기관과 통신하여 주어진 ci 가 주어진 주민등록번호의 ci 가 맞는 지 확인 합니다.
   */
  static isValidCI(ci, residentRegistrationNumber) {
    // TODO - sample 사용 기관에서 구현
    console.log(`isValidCI(${ci}, ${residentRegistrationNumber})`);
    return true;
  }

  findPatientWithBlockchainAddress(blockchainAddress) {
    for (let i = 0; i < this.patientList.length; i += 1) {
      const patient = this.patientList[i];
      if (patient.blockchainAddress === blockchainAddress) {
        return patient;
      }
    }
    return null;
  }

  findPatientWithRRN(RRN) {
    for (let i = 0; i < this.patientList.length; i += 1) {
      const patient = this.patientList[i];
      if (patient.RRN === RRN) {
        return patient;
      }
    }
    return null;
  }
}

export { Hospital as default };
