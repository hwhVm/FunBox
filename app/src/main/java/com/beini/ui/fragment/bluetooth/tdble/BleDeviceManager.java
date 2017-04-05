package com.beini.ui.fragment.bluetooth.tdble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.beini.utils.BLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BleDeviceManager {

	public List<IBlueHandle> handleList = new ArrayList<>();
	static BleDeviceManager mInstance = null;
	private ExecutorService writeService;
	private final String TAG = "BleDeviceManager";

	public static BleDeviceManager getInstance() {
		if (mInstance == null) {
			mInstance = new BleDeviceManager();
		}
		return mInstance;
	}

	public BleDeviceManager() {
		writeService = Executors.newSingleThreadExecutor();
	}

	public BluetoothDevice getDevice(int index) {
		if (index < handleList.size()) {
			return handleList.get(index).getDevice();
		}
		return null;
	}

	public void connectLastBleDevices() {
//		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
////		List<String> list = SharedPerferenceUtils.getBleDeviceList();
//		if (list == null) {
//			return;
//		}
//		for (String s : list) {
//            if(BluetoothAdapter.checkBluetoothAddress(s)) {
//				BluetoothDevice device = adapter.getRemoteDevice(s);
//				connectAsync(device);
//            }
//		}
	}

	private void connectAsync(final BluetoothDevice device) {
//		Observable.create(new Observable.OnSubscribe<Boolean>() {
//			@Override
//			public void call(Subscriber<? super Boolean> subscriber) {
//				IBlueHandle handle = new SPPHandle();
////                IBlueHandle handle = new BLEHandle();
//				handle.start(device);
//				if (handle.connect()) {
//					handleList.add(handle);
//				}
//			}
//		}).subscribeOn(Schedulers.newThread())
//				.subscribe(new Action1<Boolean>() {
//					@Override
//					public void call(Boolean aBoolean) {
//
//					}
//				});
	}


	public int size() {
		return handleList.size();
	}

	public boolean connect(BluetoothDevice device) {
		IBlueHandle handle = new SPPHandle();
//        IBlueHandle handle = new BLEHandle();
		handle.start(device);
		if (!handle.connect()) {
			return false;
		} else {
			handleList.add(handle);
			List<String> nameList = new ArrayList<>();
			for (IBlueHandle tmp_handle : handleList) {
				nameList.add(tmp_handle.getDevice().getAddress());
			}
//			SharedPerferenceUtils.setBleDeviceList(nameList);
		}
		return true;
	}

	public void closeAll() {
		for (int i = 0; i < handleList.size(); i++) {
			handleList.get(i).close();
		}
		for (int i = 0; i < handleList.size(); i++) {
			handleList.remove(i);

		}
	}

	public void close(BluetoothDevice device) {
		for (int i = 0; i < handleList.size(); i++) {
			if (handleList.get(i).getDevice().getAddress().equals(device.getAddress())) {
				handleList.get(i).close();
				handleList.remove(i);

				List<String> nameList = new ArrayList<>();
				for (IBlueHandle tmp_handle : handleList) {
                    if(BluetoothAdapter.checkBluetoothAddress(tmp_handle.getDevice().getAddress())) {
						nameList.add(tmp_handle.getDevice().getAddress());
					}
				}
//				SharedPerferenceUtils.setBleDeviceList(nameList);

				return;
			}
		}
	}

	public void writeAll(final byte[] data) {// 向ConnectedThread写入数据的方法
		writeService.execute(new Runnable() {
			@Override
			public void run() {
				BLog.d("发出消息 write " + data.length);
				for (int i = 0; i < handleList.size(); i++) {
					handleList.get(i).write(data);
				}
			}
		});
	}

	/**
	 * 根据位置发送命令
	 * @param data
	 * @param postion
	 */
	public void writeByPostion(final byte[] data, final int postion) {
//		BLog.e("--------------------->postion=" + postion + "  handleList.size()=" + handleList.size());
		writeService.execute(new Runnable() {
			@Override
			public void run() {
				if (handleList != null && handleList.size()>= postion) {
					handleList.get(postion-1).write(data);
				}
			}
		});
	}

}
