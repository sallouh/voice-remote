/**
 * Copyright 2012, Malamber
 * http://www.malamber.org/android
 */

package org.malamber.voice.sevice;
import org.malamber.voice.aidl.IMediaPlaybackService;

import android.os.RemoteException;

public class PlaybackService extends IMediaPlaybackService.Stub {

	@Override
	public void openFile(String path, boolean oneShot) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openFileAsync(String path) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void open(long[] list, int position) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getQueuePosition() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPlaying() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void play() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prev() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long duration() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long position() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long seek(long pos) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTrackName() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAlbumName() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getAlbumId() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getArtistName() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getArtistId() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void enqueue(long[] list, int action) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long[] getQueue() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moveQueueItem(int from, int to) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setQueuePosition(int index) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPath() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getAudioId() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setShuffleMode(int shufflemode) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getShuffleMode() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeTracks(int first, int last) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeTrack(long id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRepeatMode(int repeatmode) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRepeatMode() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMediaMountedCount() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
