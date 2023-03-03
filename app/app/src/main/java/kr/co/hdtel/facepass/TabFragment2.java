package kr.co.hdtel.facepass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kr.co.hdtel.facepass.databinding.TabFragment2Binding;
import kr.co.hdtel.facepass.newData.FaceItem2;
import kr.co.hdtel.facepass.newData.NewAdapter2;
import kr.co.hdtel.facepass.newData.viewmodel.FaceListViewModel;
import kr.co.hdtel.server.linux.Device;
import kr.co.hdtel.server.linux.face.FaceRecognition;

public class TabFragment2 extends Fragment {
    private TabFragment2Binding binding;
    private ArrayList<FaceItem2> mFaceItems2;
    private NewAdapter2 mNewAdapter2;
    private FaceListViewModel mFaceListViewModel;
    LinearLayout linearLayout;
    LinearLayout grLinearLayout;
    ImageButton close;
    TextView clicked;
    TextView all;
    CheckBox checkBox;
    int pos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.tab_fragment2, container, false);
        linearLayout = binding.linearLayout;
        grLinearLayout = binding.greenLinearLayout;
        close = binding.close;
        clicked = binding.clickedText;
        all = binding.allText;
        mFaceItems2 = new ObservableArrayList<>();
        mFaceListViewModel = new ViewModelProvider(requireActivity()).get(FaceListViewModel.class);
        Device.get(requireContext()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Device>() {
            @Override
            public void accept(Device device) throws Exception {
                mFaceListViewModel.getVisitorFace(requireContext(), FaceRecognition.Type.VISITOR, device.getBuilding(), device.getRoom());
            }
        });
        mNewAdapter2 = new NewAdapter2(mFaceItems2, linearLayout, grLinearLayout, close, clicked, all);
        mNewAdapter2.setOnItemClickListener(new NewAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                checkBox = (CheckBox) v;
                pos = position;
                binding.used.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(view.getId() == R.id.used) {
                            mFaceItems2.get(pos).setApproveYN("사용");
                            mFaceListViewModel.updateFace(requireContext(), mFaceItems2.get(pos).getFaceId(), mFaceItems2.get(pos).getRequestId(), true);
                            linearLayout.setVisibility(View.VISIBLE);
                            grLinearLayout.setVisibility(View.GONE);
                        }
                        mNewAdapter2.notifyItemChanged(pos);
                    }
                });
                binding.unused.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(view.getId() == R.id.unused) {
                            mFaceItems2.get(pos).setApproveYN("사용 안함");
                            mFaceListViewModel.updateFace(requireContext(), mFaceItems2.get(pos).getFaceId(), mFaceItems2.get(pos).getRequestId(), false);
                            linearLayout.setVisibility(View.VISIBLE);
                            grLinearLayout.setVisibility(View.GONE);
                        }
                        mNewAdapter2.notifyItemChanged(pos);
                    }
                });
                binding.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(view.getId() == R.id.delete) {
                            mFaceListViewModel.deleteFace(requireContext(), mFaceItems2.get(pos).getFaceId());
                            mFaceItems2.remove(pos);
                            checkBox.setChecked(false);
                            linearLayout.setVisibility(View.VISIBLE);
                            grLinearLayout.setVisibility(View.GONE);
                        }
                        mNewAdapter2.notifyItemChanged(pos);
                    }
                });
            }
        });
        binding.recyclerView.setAdapter(mNewAdapter2);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFaceListViewModel.getFaceItems2().observe(getViewLifecycleOwner(), new Observer<List<FaceItem2>>() {
            @Override
            public void onChanged(List<FaceItem2> faceItems2) {
                mFaceItems2 = (ArrayList<FaceItem2>) faceItems2;
                binding.setFaceList(mFaceItems2);
                mNewAdapter2.notifyDataSetChanged();
            }
        });

    }
}
